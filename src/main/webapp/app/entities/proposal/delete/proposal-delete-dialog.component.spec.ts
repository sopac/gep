jest.mock('@ng-bootstrap/ng-bootstrap');

import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ProposalService } from '../service/proposal.service';

import { ProposalDeleteDialogComponent } from './proposal-delete-dialog.component';

describe('Component Tests', () => {
  describe('Proposal Management Delete Component', () => {
    let comp: ProposalDeleteDialogComponent;
    let fixture: ComponentFixture<ProposalDeleteDialogComponent>;
    let service: ProposalService;
    let mockActiveModal: NgbActiveModal;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProposalDeleteDialogComponent],
        providers: [NgbActiveModal],
      })
        .overrideTemplate(ProposalDeleteDialogComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ProposalDeleteDialogComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ProposalService);
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
