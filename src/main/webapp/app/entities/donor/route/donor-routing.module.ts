import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DonorComponent } from '../list/donor.component';
import { DonorDetailComponent } from '../detail/donor-detail.component';
import { DonorUpdateComponent } from '../update/donor-update.component';
import { DonorRoutingResolveService } from './donor-routing-resolve.service';

const donorRoute: Routes = [
  {
    path: '',
    component: DonorComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: DonorDetailComponent,
    resolve: {
      donor: DonorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: DonorUpdateComponent,
    resolve: {
      donor: DonorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: DonorUpdateComponent,
    resolve: {
      donor: DonorRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(donorRoute)],
  exports: [RouterModule],
})
export class DonorRoutingModule {}
