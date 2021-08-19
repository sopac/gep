import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProposal, getProposalIdentifier } from '../proposal.model';

export type EntityResponseType = HttpResponse<IProposal>;
export type EntityArrayResponseType = HttpResponse<IProposal[]>;

@Injectable({ providedIn: 'root' })
export class ProposalService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/proposals');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(proposal: IProposal): Observable<EntityResponseType> {
    return this.http.post<IProposal>(this.resourceUrl, proposal, { observe: 'response' });
  }

  update(proposal: IProposal): Observable<EntityResponseType> {
    return this.http.put<IProposal>(`${this.resourceUrl}/${getProposalIdentifier(proposal) as number}`, proposal, { observe: 'response' });
  }

  partialUpdate(proposal: IProposal): Observable<EntityResponseType> {
    return this.http.patch<IProposal>(`${this.resourceUrl}/${getProposalIdentifier(proposal) as number}`, proposal, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProposal>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProposal[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addProposalToCollectionIfMissing(proposalCollection: IProposal[], ...proposalsToCheck: (IProposal | null | undefined)[]): IProposal[] {
    const proposals: IProposal[] = proposalsToCheck.filter(isPresent);
    if (proposals.length > 0) {
      const proposalCollectionIdentifiers = proposalCollection.map(proposalItem => getProposalIdentifier(proposalItem)!);
      const proposalsToAdd = proposals.filter(proposalItem => {
        const proposalIdentifier = getProposalIdentifier(proposalItem);
        if (proposalIdentifier == null || proposalCollectionIdentifiers.includes(proposalIdentifier)) {
          return false;
        }
        proposalCollectionIdentifiers.push(proposalIdentifier);
        return true;
      });
      return [...proposalsToAdd, ...proposalCollection];
    }
    return proposalCollection;
  }
}
