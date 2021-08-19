import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IResource } from '../resource.model';
import { DataUtils } from 'app/core/util/data-util.service';

@Component({
  selector: 'jhi-resource-detail',
  templateUrl: './resource-detail.component.html',
})
export class ResourceDetailComponent implements OnInit {
  resource: IResource | null = null;

  constructor(protected dataUtils: DataUtils, protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ resource }) => {
      this.resource = resource;
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
