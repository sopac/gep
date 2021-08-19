import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'country',
        data: { pageTitle: 'Countries' },
        loadChildren: () => import('./country/country.module').then(m => m.CountryModule),
      },
      {
        path: 'organisation',
        data: { pageTitle: 'Organisations' },
        loadChildren: () => import('./organisation/organisation.module').then(m => m.OrganisationModule),
      },
      {
        path: 'project',
        data: { pageTitle: 'Projects' },
        loadChildren: () => import('./project/project.module').then(m => m.ProjectModule),
      },
      {
        path: 'resource',
        data: { pageTitle: 'Resources' },
        loadChildren: () => import('./resource/resource.module').then(m => m.ResourceModule),
      },
      {
        path: 'proposal',
        data: { pageTitle: 'Proposals' },
        loadChildren: () => import('./proposal/proposal.module').then(m => m.ProposalModule),
      },
      {
        path: 'donor',
        data: { pageTitle: 'Donors' },
        loadChildren: () => import('./donor/donor.module').then(m => m.DonorModule),
      },
      {
        path: 'contact',
        data: { pageTitle: 'Contacts' },
        loadChildren: () => import('./contact/contact.module').then(m => m.ContactModule),
      },
      {
        path: 'tool',
        data: { pageTitle: 'Tools' },
        loadChildren: () => import('./tool/tool.module').then(m => m.ToolModule),
      },
      {
        path: 'team',
        data: { pageTitle: 'Teams' },
        loadChildren: () => import('./team/team.module').then(m => m.TeamModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
