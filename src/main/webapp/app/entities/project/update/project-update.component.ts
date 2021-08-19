import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProject, Project } from '../project.model';
import { ProjectService } from '../service/project.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IResource } from 'app/entities/resource/resource.model';
import { ResourceService } from 'app/entities/resource/service/resource.service';
import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';
import { ITeam } from 'app/entities/team/team.model';
import { TeamService } from 'app/entities/team/service/team.service';
import { IDonor } from 'app/entities/donor/donor.model';
import { DonorService } from 'app/entities/donor/service/donor.service';
import { IContact } from 'app/entities/contact/contact.model';
import { ContactService } from 'app/entities/contact/service/contact.service';

@Component({
  selector: 'jhi-project-update',
  templateUrl: './project-update.component.html',
})
export class ProjectUpdateComponent implements OnInit {
  isSaving = false;

  resourcesSharedCollection: IResource[] = [];
  countriesSharedCollection: ICountry[] = [];
  teamsSharedCollection: ITeam[] = [];
  donorsSharedCollection: IDonor[] = [];
  contactsSharedCollection: IContact[] = [];

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required]],
    startDate: [],
    endDate: [],
    status: [],
    totalBudgetCurrency: [],
    totalBudget: [],
    totalBudgetBreakdown: [],
    sustainableDevelopmentGoal: [],
    frameworkResilientDevelopmentPacific: [],
    fullCostRecoveryCoverage: [],
    resources: [],
    countries: [],
    team: [],
    donor: [],
    focalContact: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected projectService: ProjectService,
    protected resourceService: ResourceService,
    protected countryService: CountryService,
    protected teamService: TeamService,
    protected donorService: DonorService,
    protected contactService: ContactService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ project }) => {
      this.updateForm(project);

      this.loadRelationshipsOptions();
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  setFileData(event: Event, field: string, isImage: boolean): void {
    this.dataUtils.loadFileToForm(event, this.editForm, field, isImage).subscribe({
      error: (err: FileLoadError) =>
        this.eventManager.broadcast(
          new EventWithContent<AlertError>('gepApp.error', { message: err.message })
        ),
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const project = this.createFromForm();
    if (project.id !== undefined) {
      this.subscribeToSaveResponse(this.projectService.update(project));
    } else {
      this.subscribeToSaveResponse(this.projectService.create(project));
    }
  }

  trackResourceById(index: number, item: IResource): number {
    return item.id!;
  }

  trackCountryById(index: number, item: ICountry): number {
    return item.id!;
  }

  trackTeamById(index: number, item: ITeam): number {
    return item.id!;
  }

  trackDonorById(index: number, item: IDonor): number {
    return item.id!;
  }

  trackContactById(index: number, item: IContact): number {
    return item.id!;
  }

  getSelectedResource(option: IResource, selectedVals?: IResource[]): IResource {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  getSelectedCountry(option: ICountry, selectedVals?: ICountry[]): ICountry {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProject>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(project: IProject): void {
    this.editForm.patchValue({
      id: project.id,
      title: project.title,
      startDate: project.startDate,
      endDate: project.endDate,
      status: project.status,
      totalBudgetCurrency: project.totalBudgetCurrency,
      totalBudget: project.totalBudget,
      totalBudgetBreakdown: project.totalBudgetBreakdown,
      sustainableDevelopmentGoal: project.sustainableDevelopmentGoal,
      frameworkResilientDevelopmentPacific: project.frameworkResilientDevelopmentPacific,
      fullCostRecoveryCoverage: project.fullCostRecoveryCoverage,
      resources: project.resources,
      countries: project.countries,
      team: project.team,
      donor: project.donor,
      focalContact: project.focalContact,
    });

    this.resourcesSharedCollection = this.resourceService.addResourceToCollectionIfMissing(
      this.resourcesSharedCollection,
      ...(project.resources ?? [])
    );
    this.countriesSharedCollection = this.countryService.addCountryToCollectionIfMissing(
      this.countriesSharedCollection,
      ...(project.countries ?? [])
    );
    this.teamsSharedCollection = this.teamService.addTeamToCollectionIfMissing(this.teamsSharedCollection, project.team);
    this.donorsSharedCollection = this.donorService.addDonorToCollectionIfMissing(this.donorsSharedCollection, project.donor);
    this.contactsSharedCollection = this.contactService.addContactToCollectionIfMissing(
      this.contactsSharedCollection,
      project.focalContact
    );
  }

  protected loadRelationshipsOptions(): void {
    this.resourceService
      .query()
      .pipe(map((res: HttpResponse<IResource[]>) => res.body ?? []))
      .pipe(
        map((resources: IResource[]) =>
          this.resourceService.addResourceToCollectionIfMissing(resources, ...(this.editForm.get('resources')!.value ?? []))
        )
      )
      .subscribe((resources: IResource[]) => (this.resourcesSharedCollection = resources));

    this.countryService
      .query()
      .pipe(map((res: HttpResponse<ICountry[]>) => res.body ?? []))
      .pipe(
        map((countries: ICountry[]) =>
          this.countryService.addCountryToCollectionIfMissing(countries, ...(this.editForm.get('countries')!.value ?? []))
        )
      )
      .subscribe((countries: ICountry[]) => (this.countriesSharedCollection = countries));

    this.teamService
      .query()
      .pipe(map((res: HttpResponse<ITeam[]>) => res.body ?? []))
      .pipe(map((teams: ITeam[]) => this.teamService.addTeamToCollectionIfMissing(teams, this.editForm.get('team')!.value)))
      .subscribe((teams: ITeam[]) => (this.teamsSharedCollection = teams));

    this.donorService
      .query()
      .pipe(map((res: HttpResponse<IDonor[]>) => res.body ?? []))
      .pipe(map((donors: IDonor[]) => this.donorService.addDonorToCollectionIfMissing(donors, this.editForm.get('donor')!.value)))
      .subscribe((donors: IDonor[]) => (this.donorsSharedCollection = donors));

    this.contactService
      .query()
      .pipe(map((res: HttpResponse<IContact[]>) => res.body ?? []))
      .pipe(
        map((contacts: IContact[]) =>
          this.contactService.addContactToCollectionIfMissing(contacts, this.editForm.get('focalContact')!.value)
        )
      )
      .subscribe((contacts: IContact[]) => (this.contactsSharedCollection = contacts));
  }

  protected createFromForm(): IProject {
    return {
      ...new Project(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      startDate: this.editForm.get(['startDate'])!.value,
      endDate: this.editForm.get(['endDate'])!.value,
      status: this.editForm.get(['status'])!.value,
      totalBudgetCurrency: this.editForm.get(['totalBudgetCurrency'])!.value,
      totalBudget: this.editForm.get(['totalBudget'])!.value,
      totalBudgetBreakdown: this.editForm.get(['totalBudgetBreakdown'])!.value,
      sustainableDevelopmentGoal: this.editForm.get(['sustainableDevelopmentGoal'])!.value,
      frameworkResilientDevelopmentPacific: this.editForm.get(['frameworkResilientDevelopmentPacific'])!.value,
      fullCostRecoveryCoverage: this.editForm.get(['fullCostRecoveryCoverage'])!.value,
      resources: this.editForm.get(['resources'])!.value,
      countries: this.editForm.get(['countries'])!.value,
      team: this.editForm.get(['team'])!.value,
      donor: this.editForm.get(['donor'])!.value,
      focalContact: this.editForm.get(['focalContact'])!.value,
    };
  }
}
