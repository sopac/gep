jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { DonorService } from '../service/donor.service';

import { DonorDeleteDialogComponent } from './donor-delete-dialog.component';

describe('Component Tests', () => {
  describe('Donor Management Delete Component', () => {
    let comp: DonorDeleteDialogComponent;
    let fixture: ComponentFixture<DonorDeleteDialogComponent>;
    let service: DonorService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DonorDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(DonorDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DonorDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DonorService);
      mockActiveModal = TestBed.inject(NgbActiveModal);
    });

    describe('confirmDelete', () => {
      it('Should call delete service on confirmDelete', inject(
        [],
        fakeAsync(() => {
          // GIVEN
          spyOn(service, 'delete').and.returnValue(of({}));

          // WHEN
          comp.confirmDelete(123);
          tick();

          // THEN
          expect(service.delete).toHaveBeenCalledWith(123);
          expect(mockActiveModal.close).toHaveBeenCalledWith('deleted');
        })
      ));

      it('Should not call delete service on clear', () => {
        // GIVEN
        spyOn(service, 'delete');

        // WHEN
        comp.cancel();

        // THEN
        expect(service.delete).not.toHaveBeenCalled();
        expect(mockActiveModal.close).not.toHaveBeenCalled();
        expect(mockActiveModal.dismiss).toHaveBeenCalled();
      });
    });
  });
});
