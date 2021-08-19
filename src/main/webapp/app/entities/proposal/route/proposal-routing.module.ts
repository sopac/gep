import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProposalComponent } from '../list/proposal.component';
import { ProposalDetailComponent } from '../detail/proposal-detail.component';
import { ProposalUpdateComponent } from '../update/proposal-update.component';
import { ProposalRoutingResolveService } from './proposal-routing-resolve.service';

const proposalRoute: Routes = [
  {
    path: '',
    component: ProposalComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProposalDetailComponent,
    resolve: {
      proposal: ProposalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProposalUpdateComponent,
    resolve: {
      proposal: ProposalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: ProposalUpdateComponent,
    resolve: {
      proposal: ProposalRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(proposalRoute)],
  exports: [RouterModule],
})
export class ProposalRoutingModule {}
