import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IDonor } from '../donor.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-donor-detail',
  templateUrl: './donor-detail.component.html',
})
export class DonorDetailComponent implements OnInit {
  donor: IDonor | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ donor }) => {
      this.donor = donor;
    });
  }

  byteSize(base64String: string): string {
    return this.dataUtils.byteSize(base64String);
  }

  openFile(base64String: string, contentType: string | null | undefined): void {
    this.dataUtils.openFile(base64String, contentType);
  }

  previousState(): void {
    window.history.back();
  }
}
