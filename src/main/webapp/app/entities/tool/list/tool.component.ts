import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ITool } from '../tool.model';
import { ToolService } from '../service/tool.service';
import { ToolDeleteDialogComponent } from '../delete/tool-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-tool',
  templateUrl: './tool.component.html',
})
export class ToolComponent implements OnInit {
  tools?: ITool[];
  isLoading = false;

  constructor(protected toolService: ToolService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.toolService.query().subscribe(
      (res: HttpResponse<ITool[]>) => {
        this.isLoading = false;
        this.tools = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ITool): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(tool: ITool): void {
    const modalRef = this.modalService.open(ToolDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.tool = tool;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
