import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { ProposalComponent } from './list/proposal.component';
import { ProposalDetailComponent } from './detail/proposal-detail.component';
import { ProposalUpdateComponent } from './update/proposal-update.component';
import { ProposalDeleteDialogComponent } from './delete/proposal-delete-dialog.component';
import { ProposalRoutingModule } from './route/proposal-routing.module';

@NgModule({
  imports: [SharedModule, ProposalRoutingModule],
  declarations: [ProposalComponent, ProposalDetailComponent, ProposalUpdateComponent, ProposalDeleteDialogComponent],
  entryComponents: [ProposalDeleteDialogComponent],
})
export class ProposalModule {}
