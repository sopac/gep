import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IResource } from '../resource.model';
import { ResourceService } from '../service/resource.service';

@Component({
  templateUrl: './resource-delete-dialog.component.html',
})
export class ResourceDeleteDialogComponent {
  resource?: IResource;

  constructor(protected resourceService: ResourceService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.resourceService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
