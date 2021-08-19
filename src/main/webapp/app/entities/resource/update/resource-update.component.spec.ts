jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { ResourceService } from '../service/resource.service';
import { IResource, Resource } from '../resource.model';

import { ResourceUpdateComponent } from './resource-update.component';

describe('Component Tests', () => {
  describe('Resource Management Update Component', () => {
    let comp: ResourceUpdateComponent;
    let fixture: ComponentFixture<ResourceUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let resourceService: ResourceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ResourceUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(ResourceUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ResourceUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      resourceService = TestBed.inject(ResourceService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const resource: IResource = { id: 456 };

        activatedRoute.data = of({ resource });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(resource));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const resource = { id: 123 };
        spyOn(resourceService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ resource });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: resource }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(resourceService.update).toHaveBeenCalledWith(resource);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject();
        const resource = new Resource();
        spyOn(resourceService, 'create').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ resource });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: resource }));
        saveSubject.complete();

        // THEN
        expect(resourceService.create).toHaveBeenCalledWith(resource);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject();
        const resource = { id: 123 };
        spyOn(resourceService, 'update').and.returnValue(saveSubject);
        spyOn(comp, 'previousState');
        activatedRoute.data = of({ resource });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(resourceService.update).toHaveBeenCalledWith(resource);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
