import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { Status } from 'app/entities/enumerations/status.model';
import { Currency } from 'app/entities/enumerations/currency.model';
import { IProposal, Proposal } from '../proposal.model';

import { ProposalService } from './proposal.service';

describe('Service Tests', () => {
  describe('Proposal Service', () => {
    let service: ProposalService;
    let httpMock: HttpTestingController;
    let elemDefault: IProposal;
    let expectedResult: IProposal | IProposal[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(ProposalService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        title: 'AAAAAAA',
        description: 'AAAAAAA',
        status: Status.ACTIVE,
        divsionEndorsement: false,
        pacificCommunityEndorsement: false,
        totalBudgetCurrency: Currency.USD,
        totalBudget: 0,
        totalBudgetBreakdown: 'AAAAAAA',
        keyThematicAreas: 'AAAAAAA',
        lessonsLearntBestPractices: 'AAAAAAA',
        fullCostRecoveryCoverage: 'AAAAAAA',
        notes: 'AAAAAAA',
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

      it('should create a Proposal', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Proposal()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Proposal', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            description: 'BBBBBB',
            status: 'BBBBBB',
            divsionEndorsement: true,
            pacificCommunityEndorsement: true,
            totalBudgetCurrency: 'BBBBBB',
            totalBudget: 1,
            totalBudgetBreakdown: 'BBBBBB',
            keyThematicAreas: 'BBBBBB',
            lessonsLearntBestPractices: 'BBBBBB',
            fullCostRecoveryCoverage: 'BBBBBB',
            notes: 'BBBBBB',
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Proposal', () => {
        const patchObject = Object.assign(
          {
            totalBudget: 1,
            totalBudgetBreakdown: 'BBBBBB',
            keyThematicAreas: 'BBBBBB',
            notes: 'BBBBBB',
          },
          new Proposal()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Proposal', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            title: 'BBBBBB',
            description: 'BBBBBB',
            status: 'BBBBBB',
            divsionEndorsement: true,
            pacificCommunityEndorsement: true,
            totalBudgetCurrency: 'BBBBBB',
            totalBudget: 1,
            totalBudgetBreakdown: 'BBBBBB',
            keyThematicAreas: 'BBBBBB',
            lessonsLearntBestPractices: 'BBBBBB',
            fullCostRecoveryCoverage: 'BBBBBB',
            notes: 'BBBBBB',
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

      it('should delete a Proposal', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addProposalToCollectionIfMissing', () => {
        it('should add a Proposal to an empty array', () => {
          const proposal: IProposal = { id: 123 };
          expectedResult = service.addProposalToCollectionIfMissing([], proposal);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(proposal);
        });

        it('should not add a Proposal to an array that contains it', () => {
          const proposal: IProposal = { id: 123 };
          const proposalCollection: IProposal[] = [
            {
              ...proposal,
            },
            { id: 456 },
          ];
          expectedResult = service.addProposalToCollectionIfMissing(proposalCollection, proposal);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Proposal to an array that doesn't contain it", () => {
          const proposal: IProposal = { id: 123 };
          const proposalCollection: IProposal[] = [{ id: 456 }];
          expectedResult = service.addProposalToCollectionIfMissing(proposalCollection, proposal);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(proposal);
        });

        it('should add only unique Proposal to an array', () => {
          const proposalArray: IProposal[] = [{ id: 123 }, { id: 456 }, { id: 95947 }];
          const proposalCollection: IProposal[] = [{ id: 123 }];
          expectedResult = service.addProposalToCollectionIfMissing(proposalCollection, ...proposalArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const proposal: IProposal = { id: 123 };
          const proposal2: IProposal = { id: 456 };
          expectedResult = service.addProposalToCollectionIfMissing([], proposal, proposal2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(proposal);
          expect(expectedResult).toContain(proposal2);
        });

        it('should accept null and undefined values', () => {
          const proposal: IProposal = { id: 123 };
          expectedResult = service.addProposalToCollectionIfMissing([], null, proposal, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(proposal);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
