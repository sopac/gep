import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { ResourceDetailComponent } from './resource-detail.component';

describe('Component Tests', () => {
  describe('Resource Management Detail Component', () => {
    let comp: ResourceDetailComponent;
    let fixture: ComponentFixture<ResourceDetailComponent>;
    let dataUtils: DataUtils;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [ResourceDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ resource: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(ResourceDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(ResourceDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = TestBed.inject(DataUtils);
    });

    describe('OnInit', () => {
      it('Should load resource on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.resource).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });

    describe('byteSize', () => {
      it('Should call byteSize from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'byteSize');
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.byteSize(fakeBase64);

        // THEN
        expect(dataUtils.byteSize).toBeCalledWith(fakeBase64);
      });
    });

    describe('openFile', () => {
      it('Should call openFile from DataUtils', () => {
        // GIVEN
        spyOn(dataUtils, 'openFile');
        const fakeContentType = 'fake content type';
        const fakeBase64 = 'fake base64';

        // WHEN
        comp.openFile(fakeBase64, fakeContentType);

        // THEN
        expect(dataUtils.openFile).toBeCalledWith(fakeBase64, fakeContentType);
      });
    });
  });
});
