import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IDonor } from '../donor.model';
import { DonorService } from '../service/donor.service';
import { DonorDeleteDialogComponent } from '../delete/donor-delete-dialog.component';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-donor',
  templateUrl: './donor.component.html',
})
export class DonorComponent implements OnInit {
  donors?: IDonor[];
  isLoading = false;

  constructor(protected donorService: DonorService, protected dataUtils: DataUtils, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.donorService.query().subscribe(
      (res: HttpResponse<IDonor[]>) => {
        this.isLoading = false;
        this.donors = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IDonor): number {
    return item.id!;
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    return this.dataUtils.openFile(base64String, contentType);
  }

  delete(donor: IDonor): void {
    const modalRef = this.modalService.open(DonorDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.donor = donor;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
