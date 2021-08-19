import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IProposal } from '../proposal.model';
import { ProposalService } from '../service/proposal.service';
import { ProposalDeleteDialogComponent } from '../delete/proposal-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-proposal',
  templateUrl: './proposal.component.html',
})
export class ProposalComponent implements OnInit {
  proposals?: IProposal[];
  isLoading = false;

  constructor(protected proposalService: ProposalService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.proposalService.query().subscribe(
      (res: HttpResponse<IProposal[]>) => {
        this.isLoading = false;
        this.proposals = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IProposal): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(proposal: IProposal): void {
    const modalRef = this.modalService.open(ProposalDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.proposal = proposal;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
