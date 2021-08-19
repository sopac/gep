import { NgModule } from '@angular/core';

import { SharedModule } from 'app/shared/shared.module';
import { DonorComponent } from './list/donor.component';
import { DonorDetailComponent } from './detail/donor-detail.component';
import { DonorUpdateComponent } from './update/donor-update.component';
import { DonorDeleteDialogComponent } from './delete/donor-delete-dialog.component';
import { DonorRoutingModule } from './route/donor-routing.module';

@NgModule({
  imports: [SharedModule, DonorRoutingModule],
  declarations: [DonorComponent, DonorDetailComponent, DonorUpdateComponent, DonorDeleteDialogComponent],
  entryComponents: [DonorDeleteDialogComponent],
})
export class DonorModule {}
