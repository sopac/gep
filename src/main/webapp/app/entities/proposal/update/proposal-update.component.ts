import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProposal, Proposal } from '../proposal.model';
import { ProposalService } from '../service/proposal.service';
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

@Component({
  selector: 'jhi-proposal-update',
  templateUrl: './proposal-update.component.html',
})
export class ProposalUpdateComponent implements OnInit {
  isSaving = false;

  resourcesSharedCollection: IResource[] = [];
  countriesSharedCollection: ICountry[] = [];
  teamsSharedCollection: ITeam[] = [];
  donorsSharedCollection: IDonor[] = [];

  editForm = this.fb.group({
    id: [],
    title: [null, [Validators.required]],
    description: [],
    status: [],
    divsionEndorsement: [],
    pacificCommunityEndorsement: [],
    totalBudgetCurrency: [],
    totalBudget: [],
    totalBudgetBreakdown: [],
    keyThematicAreas: [],
    lessonsLearntBestPractices: [],
    fullCostRecoveryCoverage: [],
    notes: [],
    resources: [],
    countries: [],
    team: [],
    donor: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected proposalService: ProposalService,
    protected resourceService: ResourceService,
    protected countryService: CountryService,
    protected teamService: TeamService,
    protected donorService: DonorService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ proposal }) => {
      this.updateForm(proposal);

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
    const proposal = this.createFromForm();
    if (proposal.id !== undefined) {
      this.subscribeToSaveResponse(this.proposalService.update(proposal));
    } else {
      this.subscribeToSaveResponse(this.proposalService.create(proposal));
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

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProposal>>): void {
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

  protected updateForm(proposal: IProposal): void {
    this.editForm.patchValue({
      id: proposal.id,
      title: proposal.title,
      description: proposal.description,
      status: proposal.status,
      divsionEndorsement: proposal.divsionEndorsement,
      pacificCommunityEndorsement: proposal.pacificCommunityEndorsement,
      totalBudgetCurrency: proposal.totalBudgetCurrency,
      totalBudget: proposal.totalBudget,
      totalBudgetBreakdown: proposal.totalBudgetBreakdown,
      keyThematicAreas: proposal.keyThematicAreas,
      lessonsLearntBestPractices: proposal.lessonsLearntBestPractices,
      fullCostRecoveryCoverage: proposal.fullCostRecoveryCoverage,
      notes: proposal.notes,
      resources: proposal.resources,
      countries: proposal.countries,
      team: proposal.team,
      donor: proposal.donor,
    });

    this.resourcesSharedCollection = this.resourceService.addResourceToCollectionIfMissing(
      this.resourcesSharedCollection,
      ...(proposal.resources ?? [])
    );
    this.countriesSharedCollection = this.countryService.addCountryToCollectionIfMissing(
      this.countriesSharedCollection,
      ...(proposal.countries ?? [])
    );
    this.teamsSharedCollection = this.teamService.addTeamToCollectionIfMissing(this.teamsSharedCollection, proposal.team);
    this.donorsSharedCollection = this.donorService.addDonorToCollectionIfMissing(this.donorsSharedCollection, proposal.donor);
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
  }

  protected createFromForm(): IProposal {
    return {
      ...new Proposal(),
      id: this.editForm.get(['id'])!.value,
      title: this.editForm.get(['title'])!.value,
      description: this.editForm.get(['description'])!.value,
      status: this.editForm.get(['status'])!.value,
      divsionEndorsement: this.editForm.get(['divsionEndorsement'])!.value,
      pacificCommunityEndorsement: this.editForm.get(['pacificCommunityEndorsement'])!.value,
      totalBudgetCurrency: this.editForm.get(['totalBudgetCurrency'])!.value,
      totalBudget: this.editForm.get(['totalBudget'])!.value,
      totalBudgetBreakdown: this.editForm.get(['totalBudgetBreakdown'])!.value,
      keyThematicAreas: this.editForm.get(['keyThematicAreas'])!.value,
      lessonsLearntBestPractices: this.editForm.get(['lessonsLearntBestPractices'])!.value,
      fullCostRecoveryCoverage: this.editForm.get(['fullCostRecoveryCoverage'])!.value,
      notes: this.editForm.get(['notes'])!.value,
      resources: this.editForm.get(['resources'])!.value,
      countries: this.editForm.get(['countries'])!.value,
      team: this.editForm.get(['team'])!.value,
      donor: this.editForm.get(['donor'])!.value,
    };
  }
}
