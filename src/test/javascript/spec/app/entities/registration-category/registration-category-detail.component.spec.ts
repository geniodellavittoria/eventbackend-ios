/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { EventAppTestModule } from '../../../test.module';
import { RegistrationCategoryDetailComponent } from 'app/entities/registration-category/registration-category-detail.component';
import { RegistrationCategory } from 'app/shared/model/registration-category.model';

describe('Component Tests', () => {
    describe('RegistrationCategory Management Detail Component', () => {
        let comp: RegistrationCategoryDetailComponent;
        let fixture: ComponentFixture<RegistrationCategoryDetailComponent>;
        const route = ({ data: of({ registrationCategory: new RegistrationCategory(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EventAppTestModule],
                declarations: [RegistrationCategoryDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(RegistrationCategoryDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(RegistrationCategoryDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.registrationCategory).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
