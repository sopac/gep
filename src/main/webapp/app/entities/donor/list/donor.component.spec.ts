import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { DonorService } from '../service/donor.service';

import { DonorComponent } from './donor.component';

describe('Component Tests', () => {
  describe('Donor Management Component', () => {
    let comp: DonorComponent;
    let fixture: ComponentFixture<DonorComponent>;
    let service: DonorService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [DonorComponent],
      })
        .overrideTemplate(DonorComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(DonorComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(DonorService);

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
      expect(comp.donors?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
