import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IResource } from '../resource.model';
import { ResourceService } from '../service/resource.service';
import { ResourceDeleteDialogComponent } from '../delete/resource-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-resource',
  templateUrl: './resource.component.html',
})
export class ResourceComponent implements OnInit {
  resources?: IResource[];
  isLoading = false;

  constructor(protected resourceService: ResourceService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.resourceService.query().subscribe(
      (res: HttpResponse<IResource[]>) => {
        this.isLoading = false;
        this.resources = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IResource): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(resource: IResource): void {
    const modalRef = this.modalService.open(ResourceDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.resource = resource;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
