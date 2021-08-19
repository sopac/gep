import { Component, OnInit, ElementRef } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IContact, Contact } from '../contact.model';
import { ContactService } from '../service/contact.service';
import { AlertError } from 'app/shared/alert/alert-error.model';
import { EventManager, EventWithContent } from 'app/core/util/event-manager.service';
import { DataUtils, FileLoadError } from 'app/core/util/data-util.service';
import { IOrganisation } from 'app/entities/organisation/organisation.model';
import { OrganisationService } from 'app/entities/organisation/service/organisation.service';
import { ICountry } from 'app/entities/country/country.model';
import { CountryService } from 'app/entities/country/service/country.service';

@Component({
  selector: 'jhi-contact-update',
  templateUrl: './contact-update.component.html',
})
export class ContactUpdateComponent implements OnInit {
  isSaving = false;

  organisationsSharedCollection: IOrganisation[] = [];
  countriesSharedCollection: ICountry[] = [];

  editForm = this.fb.group({
    id: [],
    active: [],
    category: [],
    photo: [],
    photoContentType: [],
    name: [null, [Validators.required]],
    staff: [],
    designation: [],
    division: [],
    field: [],
    sector: [],
    email: [],
    phone: [],
    city: [],
    linkedin: [],
    twitter: [],
    facebook: [],
    skype: [],
    membershipAffiliation: [],
    notes: [],
    organisation: [],
    country: [],
  });

  constructor(
    protected dataUtils: DataUtils,
    protected eventManager: EventManager,
    protected contactService: ContactService,
    protected organisationService: OrganisationService,
    protected countryService: CountryService,
    protected elementRef: ElementRef,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ contact }) => {
      this.updateForm(contact);

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

  clearInputImage(field: string, fieldContentType: string, idInput: string): void {
    this.editForm.patchValue({
      [field]: null,
      [fieldContentType]: null,
    });
    if (idInput && this.elementRef.nativeElement.querySelector('#' + idInput)) {
      this.elementRef.nativeElement.querySelector('#' + idInput).value = null;
    }
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const contact = this.createFromForm();
    if (contact.id !== undefined) {
      this.subscribeToSaveResponse(this.contactService.update(contact));
    } else {
      this.subscribeToSaveResponse(this.contactService.create(contact));
    }
  }

  trackOrganisationById(index: number, item: IOrganisation): number {
    return item.id!;
  }

  trackCountryById(index: number, item: ICountry): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IContact>>): void {
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

  protected updateForm(contact: IContact): void {
    this.editForm.patchValue({
      id: contact.id,
      active: contact.active,
      category: contact.category,
      photo: contact.photo,
      photoContentType: contact.photoContentType,
      name: contact.name,
      staff: contact.staff,
      designation: contact.designation,
      division: contact.division,
      field: contact.field,
      sector: contact.sector,
      email: contact.email,
      phone: contact.phone,
      city: contact.city,
      linkedin: contact.linkedin,
      twitter: contact.twitter,
      facebook: contact.facebook,
      skype: contact.skype,
      membershipAffiliation: contact.membershipAffiliation,
      notes: contact.notes,
      organisation: contact.organisation,
      country: contact.country,
    });

    this.organisationsSharedCollection = this.organisationService.addOrganisationToCollectionIfMissing(
      this.organisationsSharedCollection,
      contact.organisation
    );
    this.countriesSharedCollection = this.countryService.addCountryToCollectionIfMissing(this.countriesSharedCollection, contact.country);
  }

  protected loadRelationshipsOptions(): void {
    this.organisationService
      .query()
      .pipe(map((res: HttpResponse<IOrganisation[]>) => res.body ?? []))
      .pipe(
        map((organisations: IOrganisation[]) =>
          this.organisationService.addOrganisationToCollectionIfMissing(organisations, this.editForm.get('organisation')!.value)
        )
      )
      .subscribe((organisations: IOrganisation[]) => (this.organisationsSharedCollection = organisations));

    this.countryService
      .query()
      .pipe(map((res: HttpResponse<ICountry[]>) => res.body ?? []))
      .pipe(
        map((countries: ICountry[]) => this.countryService.addCountryToCollectionIfMissing(countries, this.editForm.get('country')!.value))
      )
      .subscribe((countries: ICountry[]) => (this.countriesSharedCollection = countries));
  }

  protected createFromForm(): IContact {
    return {
      ...new Contact(),
      id: this.editForm.get(['id'])!.value,
      active: this.editForm.get(['active'])!.value,
      category: this.editForm.get(['category'])!.value,
      photoContentType: this.editForm.get(['photoContentType'])!.value,
      photo: this.editForm.get(['photo'])!.value,
      name: this.editForm.get(['name'])!.value,
      staff: this.editForm.get(['staff'])!.value,
      designation: this.editForm.get(['designation'])!.value,
      division: this.editForm.get(['division'])!.value,
      field: this.editForm.get(['field'])!.value,
      sector: this.editForm.get(['sector'])!.value,
      email: this.editForm.get(['email'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      city: this.editForm.get(['city'])!.value,
      linkedin: this.editForm.get(['linkedin'])!.value,
      twitter: this.editForm.get(['twitter'])!.value,
      facebook: this.editForm.get(['facebook'])!.value,
      skype: this.editForm.get(['skype'])!.value,
      membershipAffiliation: this.editForm.get(['membershipAffiliation'])!.value,
      notes: this.editForm.get(['notes'])!.value,
      organisation: this.editForm.get(['organisation'])!.value,
      country: this.editForm.get(['country'])!.value,
    };
  }
}
