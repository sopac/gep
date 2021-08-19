jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ITool, Tool } from '../tool.model';
import { ToolService } from '../service/tool.service';

import { ToolRoutingResolveService } from './tool-routing-resolve.service';

describe('Service Tests', () => {
  describe('Tool routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: ToolRoutingResolveService;
    let service: ToolService;
    let resultTool: ITool | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(ToolRoutingResolveService);
      service = TestBed.inject(ToolService);
      resultTool = undefined;
    });

    describe('resolve', () => {
      it('should return ITool returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTool = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTool).toEqual({ id: 123 });
      });

      it('should return new ITool if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTool = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultTool).toEqual(new Tool());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        spyOn(service, 'find').and.returnValue(of(new HttpResponse({ body: null })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultTool = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultTool).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
