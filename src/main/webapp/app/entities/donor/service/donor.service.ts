import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IDonor, getDonorIdentifier } from '../donor.model';

export type EntityResponseType = HttpResponse<IDonor>;
export type EntityArrayResponseType = HttpResponse<IDonor[]>;

@Injectable({ providedIn: 'root' })
export class DonorService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/donors');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(donor: IDonor): Observable<EntityResponseType> {
    return this.http.post<IDonor>(this.resourceUrl, donor, { observe: 'response' });
  }

  update(donor: IDonor): Observable<EntityResponseType> {
    return this.http.put<IDonor>(`${this.resourceUrl}/${getDonorIdentifier(donor) as number}`, donor, { observe: 'response' });
  }

  partialUpdate(donor: IDonor): Observable<EntityResponseType> {
    return this.http.patch<IDonor>(`${this.resourceUrl}/${getDonorIdentifier(donor) as number}`, donor, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IDonor>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IDonor[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addDonorToCollectionIfMissing(donorCollection: IDonor[], ...donorsToCheck: (IDonor | null | undefined)[]): IDonor[] {
    const donors: IDonor[] = donorsToCheck.filter(isPresent);
    if (donors.length > 0) {
      const donorCollectionIdentifiers = donorCollection.map(donorItem => getDonorIdentifier(donorItem)!);
      const donorsToAdd = donors.filter(donorItem => {
        const donorIdentifier = getDonorIdentifier(donorItem);
        if (donorIdentifier == null || donorCollectionIdentifiers.includes(donorIdentifier)) {
          return false;
        }
        donorCollectionIdentifiers.push(donorIdentifier);
        return true;
      });
      return [...donorsToAdd, ...donorCollection];
    }
    return donorCollection;
  }
}
