import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IResource, getResourceIdentifier } from '../resource.model';

export type EntityResponseType = HttpResponse<IResource>;
export type EntityArrayResponseType = HttpResponse<IResource[]>;

@Injectable({ providedIn: 'root' })
export class ResourceService {
  public resourceUrl = this.applicationConfigService.getEndpointFor('api/resources');

  constructor(protected http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  create(resource: IResource): Observable<EntityResponseType> {
    return this.http.post<IResource>(this.resourceUrl, resource, { observe: 'response' });
  }

  update(resource: IResource): Observable<EntityResponseType> {
    return this.http.put<IResource>(`${this.resourceUrl}/${getResourceIdentifier(resource) as number}`, resource, { observe: 'response' });
  }

  partialUpdate(resource: IResource): Observable<EntityResponseType> {
    return this.http.patch<IResource>(`${this.resourceUrl}/${getResourceIdentifier(resource) as number}`, resource, {
      observe: 'response',
    });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IResource>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IResource[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addResourceToCollectionIfMissing(resourceCollection: IResource[], ...resourcesToCheck: (IResource | null | undefined)[]): IResource[] {
    const resources: IResource[] = resourcesToCheck.filter(isPresent);
    if (resources.length > 0) {
      const resourceCollectionIdentifiers = resourceCollection.map(resourceItem => getResourceIdentifier(resourceItem)!);
      const resourcesToAdd = resources.filter(resourceItem => {
        const resourceIdentifier = getResourceIdentifier(resourceItem);
        if (resourceIdentifier == null || resourceCollectionIdentifiers.includes(resourceIdentifier)) {
          return false;
        }
        resourceCollectionIdentifiers.push(resourceIdentifier);
        return true;
      });
      return [...resourcesToAdd, ...resourceCollection];
    }
    return resourceCollection;
  }
}
