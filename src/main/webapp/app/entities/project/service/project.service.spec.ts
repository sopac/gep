import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { Status } from 'app/entities/enumerations/status.model';
import { Currency } from 'app/entities/enumerations/currency.model';
import { IProject, Project } from '../project.model';

import { ProjectService } from './project.service';

describe('Service Tests', () => {
  describe('Project Service', () => {
    let service: ProjectService;
    let httpMock: HttpTestingController;
    let elemDefault: IProject;
    let expectedResult: IProject | IProject[] | boolean | null;
    let currentDate: dayjs.Dayjs;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ProjectService);
      httpMock = TestBed.inject(HttpTestingController);
      currentDate = dayjs();

      elemDefault = {
        id: 0,
        title: 'AAAAAAA',
        startDate: currentDate,
        endDate: currentDate,
        status: Status.ACTIVE,
        totalBudgetCurrency: Currency.USD,
        totalBudget: 0,
        totalBudgetBreakdown: 'AAAAAAA',
        sustainableDevelopmentGoal: 'AAAAAAA',
        frameworkResilientDevelopmentPacific: 'AAAAAAA',
        fullCostRecoveryCoverage: 'AAAAAAA',
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Project', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService
        );

        service.create(new Project()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Project', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            status: 'BBBBBB',
            totalBudgetCurrency: 'BBBBBB',
            totalBudget: 1,
            totalBudgetBreakdown: 'BBBBBB',
            sustainableDevelopmentGoal: 'BBBBBB',
            frameworkResilientDevelopmentPacific: 'BBBBBB',
            fullCostRecoveryCoverage: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Project', () => {
        const patchObject = Object.assign(
          {
            title: 'BBBBBB',
            status: 'BBBBBB',
            totalBudgetCurrency: 'BBBBBB',
            fullCostRecoveryCoverage: 'BBBBBB',
          },
          new Project()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService
        );

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Project', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            startDate: currentDate.format(DATE_FORMAT),
            endDate: currentDate.format(DATE_FORMAT),
            status: 'BBBBBB',
            totalBudgetCurrency: 'BBBBBB',
            totalBudget: 1,
            totalBudgetBreakdown: 'BBBBBB',
            sustainableDevelopmentGoal: 'BBBBBB',
            frameworkResilientDevelopmentPacific: 'BBBBBB',
            fullCostRecoveryCoverage: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            startDate: currentDate,
            endDate: currentDate,
          },
          returnedFromService
        );

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Project', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addProjectToCollectionIfMissing', () => {
        it('should add a Project to an empty array', () => {
          const project: IProject = { id: 123 };
          expectedResult = service.addProjectToCollectionIfMissing([], project);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(project);
        });

        it('should not add a Project to an array that contains it', () => {
          const project: IProject = { id: 123 };
          const projectCollection: IProject[] = [
            {
              ...project,
            },
            { id: 456 },
          ];
          expectedResult = service.addProjectToCollectionIfMissing(projectCollection, project);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Project to an array that doesn't contain it", () => {
          const project: IProject = { id: 123 };
          const projectCollection: IProject[] = [{ id: 456 }];
          expectedResult = service.addProjectToCollectionIfMissing(projectCollection, project);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(project);
        });

        it('should add only unique Project to an array', () => {
          const projectArray: IProject[] = [{ id: 123 }, { id: 456 }, { id: 69499 }];
          const projectCollection: IProject[] = [{ id: 123 }];
          expectedResult = service.addProjectToCollectionIfMissing(projectCollection, ...projectArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const project: IProject = { id: 123 };
          const project2: IProject = { id: 456 };
          expectedResult = service.addProjectToCollectionIfMissing([], project, project2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(project);
          expect(expectedResult).toContain(project2);
        });

        it('should accept null and undefined values', () => {
          const project: IProject = { id: 123 };
          expectedResult = service.addProjectToCollectionIfMissing([], null, project, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(project);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
