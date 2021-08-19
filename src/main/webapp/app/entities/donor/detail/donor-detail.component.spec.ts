import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { DataUtils } from 'app/core/util/data-util.service';

import { DonorDetailComponent } from './donor-detail.component';

describe('Component Tests', () => {
  describe('Donor Management Detail Component', () => {
    let comp: DonorDetailComponent;
    let fixture: ComponentFixture<DonorDetailComponent>;
    let dataUtils: DataUtils;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [DonorDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ donor: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(DonorDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(DonorDetailComponent);
      comp = fixture.componentInstance;
      dataUtils = TestBed.inject(DataUtils);
    });

    describe('OnInit', () => {
      it('Should load donor on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.donor).toEqual(jasmine.objectContaining({ id: 123 }));
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
