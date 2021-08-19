import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IProject } from '../project.model';
import { ProjectService } from '../service/project.service';

@Component({
  templateUrl: './project-delete-dialog.component.html',
})
export class ProjectDeleteDialogComponent {
  project?: IProject;

  constructor(protected projectService: ProjectService, public activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.projectService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}