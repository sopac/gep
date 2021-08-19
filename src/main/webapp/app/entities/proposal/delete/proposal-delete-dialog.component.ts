import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProposal } from '../proposal.model';
import { ProposalService } from '../service/proposal.service';

@Component({
  templateUrl: './proposal-delete-dialog.component.html',
})
export class ProposalDeleteDialogComponent {
  proposal?: IProposal;

  constructor(protected proposalService: ProposalService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.proposalService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
