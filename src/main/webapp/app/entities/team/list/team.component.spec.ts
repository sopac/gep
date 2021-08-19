import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { TeamService } from '../service/team.service';

import { TeamComponent } from './team.component';

describe('Component Tests', () => {
  describe('Team Management Component', () => {
    let comp: TeamComponent;
    let fixture: ComponentFixture<TeamComponent>;
    let service: TeamService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TeamComponent],
      })
        .overrideTemplate(TeamComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TeamComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(TeamService);

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
      expect(comp.teams?.[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
