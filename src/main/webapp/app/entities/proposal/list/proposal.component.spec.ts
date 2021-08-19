import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ProposalService } from '../service/proposal.service';

import { ProposalComponent } from './proposal.component';

describe('Component Tests', () => {
  describe('Proposal Management Component', () => {
    let comp: ProposalComponent;
    let fixture: ComponentFixture<ProposalComponent>;
    let service: ProposalService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ProposalComponent],
      })
        .overrideTemplate(ProposalComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ProposalComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ProposalService);

      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.proposals?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
