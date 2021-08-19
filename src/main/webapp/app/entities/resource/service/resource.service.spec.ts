import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IResource, Resource } from '../resource.model';

import { ResourceService } from './resource.service';

describe('Service Tests', () => {
  describe('Resource Service', () => {
    let service: ResourceService;
    let httpMock: HttpTestingController;
    let elemDefault: IResource;
    let expectedResult: IResource | IResource[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ResourceService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        url: 'AAAAAAA',
        fileContentType: 'image/png',
        file: 'AAAAAAA',
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

      it('should create a Resource', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Resource()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Resource', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            url: 'BBBBBB',
            file: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Resource', () => {
        const patchObject = Object.assign(
          {
            name: 'BBBBBB',
            url: 'BBBBBB',
            file: 'BBBBBB',
          },
          new Resource()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Resource', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            url: 'BBBBBB',
            file: 'BBBBBB',
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

      it('should delete a Resource', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addResourceToCollectionIfMissing', () => {
        it('should add a Resource to an empty array', () => {
          const resource: IResource = { id: 123 };
          expectedResult = service.addResourceToCollectionIfMissing([], resource);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(resource);
        });

        it('should not add a Resource to an array that contains it', () => {
          const resource: IResource = { id: 123 };
          const resourceCollection: IResource[] = [
            {
              ...resource,
            },
            { id: 456 },
          ];
          expectedResult = service.addResourceToCollectionIfMissing(resourceCollection, resource);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Resource to an array that doesn't contain it", () => {
          const resource: IResource = { id: 123 };
          const resourceCollection: IResource[] = [{ id: 456 }];
          expectedResult = service.addResourceToCollectionIfMissing(resourceCollection, resource);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(resource);
        });

        it('should add only unique Resource to an array', () => {
          const resourceArray: IResource[] = [{ id: 123 }, { id: 456 }, { id: 66151 }];
          const resourceCollection: IResource[] = [{ id: 123 }];
          expectedResult = service.addResourceToCollectionIfMissing(resourceCollection, ...resourceArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const resource: IResource = { id: 123 };
          const resource2: IResource = { id: 456 };
          expectedResult = service.addResourceToCollectionIfMissing([], resource, resource2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(resource);
          expect(expectedResult).toContain(resource2);
        });

        it('should accept null and undefined values', () => {
          const resource: IResource = { id: 123 };
          expectedResult = service.addResourceToCollectionIfMissing([], null, resource, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(resource);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
