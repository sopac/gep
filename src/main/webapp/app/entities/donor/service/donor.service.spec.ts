import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IDonor, Donor } from '../donor.model';

import { DonorService } from './donor.service';

describe('Service Tests', () => {
  describe('Donor Service', () => {
    let service: DonorService;
    let httpMock: HttpTestingController;
    let elemDefault: IDonor;
    let expectedResult: IDonor | IDonor[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(DonorService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        description: 'AAAAAAA',
        url: 'AAAAAAA',
        city: 'AAAAAAA',
        donorCategory: 'AAAAAAA',
        sector: 'AAAAAAA',
        keyContact: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Donor', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Donor()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Donor', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
            url: 'BBBBBB',
            city: 'BBBBBB',
            donorCategory: 'BBBBBB',
            sector: 'BBBBBB',
            keyContact: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Donor', () => {
        const patchObject = Object.assign(
          {
            city: 'BBBBBB',
          },
          new Donor()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Donor', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
            url: 'BBBBBB',
            city: 'BBBBBB',
            donorCategory: 'BBBBBB',
            sector: 'BBBBBB',
            keyContact: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Donor', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addDonorToCollectionIfMissing', () => {
        it('should add a Donor to an empty array', () => {
          const donor: IDonor = { id: 123 };
          expectedResult = service.addDonorToCollectionIfMissing([], donor);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(donor);
        });

        it('should not add a Donor to an array that contains it', () => {
          const donor: IDonor = { id: 123 };
          const donorCollection: IDonor[] = [
            {
              ...donor,
            },
            { id: 456 },
          ];
          expectedResult = service.addDonorToCollectionIfMissing(donorCollection, donor);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Donor to an array that doesn't contain it", () => {
          const donor: IDonor = { id: 123 };
          const donorCollection: IDonor[] = [{ id: 456 }];
          expectedResult = service.addDonorToCollectionIfMissing(donorCollection, donor);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(donor);
        });

        it('should add only unique Donor to an array', () => {
          const donorArray: IDonor[] = [{ id: 123 }, { id: 456 }, { id: 88571 }];
          const donorCollection: IDonor[] = [{ id: 123 }];
          expectedResult = service.addDonorToCollectionIfMissing(donorCollection, ...donorArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const donor: IDonor = { id: 123 };
          const donor2: IDonor = { id: 456 };
          expectedResult = service.addDonorToCollectionIfMissing([], donor, donor2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(donor);
          expect(expectedResult).toContain(donor2);
        });

        it('should accept null and undefined values', () => {
          const donor: IDonor = { id: 123 };
          expectedResult = service.addDonorToCollectionIfMissing([], null, donor, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(donor);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
