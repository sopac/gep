import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IDonor, Donor } from '../donor.model';
import { DonorService } from '../service/donor.service';

@Injectable({ providedIn: 'root' })
export class DonorRoutingResolveService implements Resolve<IDonor> {
  constructor(protected service: DonorService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IDonor> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((donor: HttpResponse<Donor>) => {
          if (donor.body) {
            return of(donor.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Donor());
  }
}
