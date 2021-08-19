import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { ResourceService } from '../service/resource.service';

import { ResourceComponent } from './resource.component';

describe('Component Tests', () => {
  describe('Resource Management Component', () => {
    let comp: ResourceComponent;
    let fixture: ComponentFixture<ResourceComponent>;
    let service: ResourceService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [ResourceComponent],
      })
        .overrideTemplate(ResourceComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(ResourceComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(ResourceService);

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
      expect(comp.resources?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
