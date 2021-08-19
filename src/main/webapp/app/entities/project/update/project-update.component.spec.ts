jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ProjectService } from '../service/project.service';
import { IProject, Project } from '../project.model';
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

import { ProjectUpdateComponent } from './project-update.component';

describe('Component Tests', () => {
  describe('Project Management Update Component', () => {
    let comp: ProjectUpdateComponent;
    let fixture: ComponentFixture<ProjectUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let projectService: ProjectService;
    let resourceService: ResourceService;
    let countryService: CountryService;
    let teamService: TeamService;
    let donorService: DonorService;
    let contactService: ContactService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProjectUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ProjectUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProjectUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      projectService = TestBed.inject(ProjectService);
      resourceService = TestBed.inject(ResourceService);
      countryService = TestBed.inject(CountryService);
      teamService = TestBed.inject(TeamService);
      donorService = TestBed.inject(DonorService);
      contactService = TestBed.inject(ContactService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should call Resource query and add missing value', () => {
        const project: IProject = { id: 456 };
        const resources: IResource[] = [{ id: 92456 }];
        project.resources = resources;

        const resourceCollection: IResource[] = [{ id: 48664 }];
        spyOn(resourceService, 'query').and.returnValue(of(new HttpResponse({ body: resourceCollection })));
        const additionalResources = [...resources];
        const expectedCollection: IResource[] = [...additionalResources, ...resourceCollection];
        spyOn(resourceService, 'addResourceToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ project });
        comp.ngOnInit();

        expect(resourceService.query).toHaveBeenCalled();
        expect(resourceService.addResourceToCollectionIfMissing).toHaveBeenCalledWith(resourceCollection, ...additionalResources);
        expect(comp.resourcesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Country query and add missing value', () => {
        const project: IProject = { id: 456 };
        const countries: ICountry[] = [{ id: 94597 }];
        project.countries = countries;

        const countryCollection: ICountry[] = [{ id: 7467 }];
        spyOn(countryService, 'query').and.returnValue(of(new HttpResponse({ body: countryCollection })));
        const additionalCountries = [...countries];
        const expectedCollection: ICountry[] = [...additionalCountries, ...countryCollection];
        spyOn(countryService, 'addCountryToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ project });
        comp.ngOnInit();

        expect(countryService.query).toHaveBeenCalled();
        expect(countryService.addCountryToCollectionIfMissing).toHaveBeenCalledWith(countryCollection, ...additionalCountries);
        expect(comp.countriesSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Team query and add missing value', () => {
        const project: IProject = { id: 456 };
        const team: ITeam = { id: 61566 };
        project.team = team;

        const teamCollection: ITeam[] = [{ id: 76075 }];
        spyOn(teamService, 'query').and.returnValue(of(new HttpResponse({ body: teamCollection })));
        const additionalTeams = [team];
        const expectedCollection: ITeam[] = [...additionalTeams, ...teamCollection];
        spyOn(teamService, 'addTeamToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ project });
        comp.ngOnInit();

        expect(teamService.query).toHaveBeenCalled();
        expect(teamService.addTeamToCollectionIfMissing).toHaveBeenCalledWith(teamCollection, ...additionalTeams);
        expect(comp.teamsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Donor query and add missing value', () => {
        const project: IProject = { id: 456 };
        const donor: IDonor = { id: 43370 };
        project.donor = donor;

        const donorCollection: IDonor[] = [{ id: 34784 }];
        spyOn(donorService, 'query').and.returnValue(of(new HttpResponse({ body: donorCollection })));
        const additionalDonors = [donor];
        const expectedCollection: IDonor[] = [...additionalDonors, ...donorCollection];
        spyOn(donorService, 'addDonorToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ project });
        comp.ngOnInit();

        expect(donorService.query).toHaveBeenCalled();
        expect(donorService.addDonorToCollectionIfMissing).toHaveBeenCalledWith(donorCollection, ...additionalDonors);
        expect(comp.donorsSharedCollection).toEqual(expectedCollection);
      });

      it('Should call Contact query and add missing value', () => {
        const project: IProject = { id: 456 };
        const focalContact: IContact = { id: 56194 };
        project.focalContact = focalContact;

        const contactCollection: IContact[] = [{ id: 56255 }];
        spyOn(contactService, 'query').and.returnValue(of(new HttpResponse({ body: contactCollection })));
        const additionalContacts = [focalContact];
        const expectedCollection: IContact[] = [...additionalContacts, ...contactCollection];
        spyOn(contactService, 'addContactToCollectionIfMissing').and.returnValue(expectedCollection);

        activatedRoute.data = of({ project });
        comp.ngOnInit();

        expect(contactService.query).toHaveBeenCalled();
        expect(contactService.addContactToCollectionIfMissing).toHaveBeenCalledWith(contactCollection, ...additionalContacts);
        expect(comp.contactsSharedCollection).toEqual(expectedCollection);
      });

      it('Should update editForm', () => {
        const project: IProject = { id: 456 };
        const resources: IResource = { id: 60970 };
        project.resources = [resources];
        const countries: ICountry = { id: 92718 };
        project.countries = [countries];
        const team: ITeam = { id: 58438 };
        project.team = team;
        const donor: IDonor = { id: 20366 };
        project.donor = donor;
        const focalContact: IContact = { id: 88663 };
        project.focalContact = focalContact;

        activatedRoute.data = of({ project });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(project));
        expect(comp.resourcesSharedCollection).toContain(resources);
        expect(comp.countriesSharedCollection).toContain(countries);
        expect(comp.teamsSharedCollection).toContain(team);
        expect(comp.donorsSharedCollection).toContain(donor);
        expect(comp.contactsSharedCollection).toContain(focalContact);
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const project = { id: 123 };
        spyOn(projectService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ project });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: project }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(projectService.update).toHaveBeenCalledWith(project);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const project = new Project();
        spyOn(projectService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ project });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: project }));
        saveSubject.complete();

        // THEN
        expect(projectService.create).toHaveBeenCalledWith(project);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const project = { id: 123 };
        spyOn(projectService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ project });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(projectService.update).toHaveBeenCalledWith(project);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });

    describe('Tracking relationships identifiers', () => {
      describe('trackResourceById', () => {
        it('Should return tracked Resource primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackResourceById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackCountryById', () => {
        it('Should return tracked Country primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackCountryById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackTeamById', () => {
        it('Should return tracked Team primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackTeamById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackDonorById', () => {
        it('Should return tracked Donor primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackDonorById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });

      describe('trackContactById', () => {
        it('Should return tracked Contact primary key', () => {
          const entity = { id: 123 };
          const trackResult = comp.trackContactById(0, entity);
          expect(trackResult).toEqual(entity.id);
        });
      });
    });

    describe('Getting selected relationships', () => {
      describe('getSelectedResource', () => {
        it('Should return option if no Resource is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedResource(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Resource for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedResource(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Resource is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedResource(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });

      describe('getSelectedCountry', () => {
        it('Should return option if no Country is selected', () => {
          const option = { id: 123 };
          const result = comp.getSelectedCountry(option);
          expect(result === option).toEqual(true);
        });

        it('Should return selected Country for according option', () => {
          const option = { id: 123 };
          const selected = { id: 123 };
          const selected2 = { id: 456 };
          const result = comp.getSelectedCountry(option, [selected2, selected]);
          expect(result === selected).toEqual(true);
          expect(result === selected2).toEqual(false);
          expect(result === option).toEqual(false);
        });

        it('Should return option if this Country is not selected', () => {
          const option = { id: 123 };
          const selected = { id: 456 };
          const result = comp.getSelectedCountry(option, [selected]);
          expect(result === option).toEqual(true);
          expect(result === selected).toEqual(false);
        });
      });
    });
  });
});
