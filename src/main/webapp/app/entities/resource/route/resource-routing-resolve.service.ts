import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IResource, Resource } from '../resource.model';
import { ResourceService } from '../service/resource.service';

@Injectable({ providedIn: 'root' })
export class ResourceRoutingResolveService implements Resolve<IResource> {
  constructor(protected service: ResourceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IResource> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((resource: HttpResponse<Resource>) => {
          if (resource.body) {
            return of(resource.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Resource());
  }
}
