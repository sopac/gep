import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IDonor } from '../donor.model';
import { DonorService } from '../service/donor.service';

@Component({
  templateUrl: './donor-delete-dialog.component.html',
})
export class DonorDeleteDialogComponent {
  donor?: IDonor;

  constructor(protected donorService: DonorService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.donorService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
