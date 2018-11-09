/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { EventAppTestModule } from '../../../test.module';
import { ImpressionComponent } from 'app/entities/impression/impression.component';
import { ImpressionService } from 'app/entities/impression/impression.service';
import { Impression } from 'app/shared/model/impression.model';

describe('Component Tests', () => {
    describe('Impression Management Component', () => {
        let comp: ImpressionComponent;
        let fixture: ComponentFixture<ImpressionComponent>;
        let service: ImpressionService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EventAppTestModule],
                declarations: [ImpressionComponent],
                providers: []
            })
                .overrideTemplate(ImpressionComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ImpressionComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ImpressionService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new Impression(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.impressions[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
