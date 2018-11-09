/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EventAppTestModule } from '../../../test.module';
import { ImpressionDetailComponent } from 'app/entities/impression/impression-detail.component';
import { Impression } from 'app/shared/model/impression.model';

describe('Component Tests', () => {
    describe('Impression Management Detail Component', () => {
        let comp: ImpressionDetailComponent;
        let fixture: ComponentFixture<ImpressionDetailComponent>;
        const route = ({ data: of({ impression: new Impression(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EventAppTestModule],
                declarations: [ImpressionDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(ImpressionDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(ImpressionDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.impression).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
