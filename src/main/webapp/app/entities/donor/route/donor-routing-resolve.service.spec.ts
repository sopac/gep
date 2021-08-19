jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IDonor, Donor } from '../donor.model';
import { DonorService } from '../service/donor.service';

import { DonorRoutingResolveService } from './donor-routing-resolve.service';

describe('Service Tests', () => {
  describe('Donor routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: DonorRoutingResolveService;
    let service: DonorService;
    let resultDonor: IDonor | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(DonorRoutingResolveService);
      service = TestBed.inject(DonorService);
      resultDonor = undefined;
    });

    describe('resolve', () => {
      it('should return IDonor returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDonor = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDonor).toEqual({ id: 123 });
      });

      it('should return new IDonor if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDonor = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultDonor).toEqual(new Donor());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultDonor = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultDonor).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
