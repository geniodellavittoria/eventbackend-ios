/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { Observable, of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { EventAppTestModule } from '../../../test.module';
import { RegistrationCategoryComponent } from 'app/entities/registration-category/registration-category.component';
import { RegistrationCategoryService } from 'app/entities/registration-category/registration-category.service';
import { RegistrationCategory } from 'app/shared/model/registration-category.model';

describe('Component Tests', () => {
    describe('RegistrationCategory Management Component', () => {
        let comp: RegistrationCategoryComponent;
        let fixture: ComponentFixture<RegistrationCategoryComponent>;
        let service: RegistrationCategoryService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EventAppTestModule],
                declarations: [RegistrationCategoryComponent],
                providers: []
            })
                .overrideTemplate(RegistrationCategoryComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RegistrationCategoryComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RegistrationCategoryService);
        });

        it('Should call load all on init', () => {
            // GIVEN
            const headers = new HttpHeaders().append('link', 'link;link');
            spyOn(service, 'query').and.returnValue(
                of(
                    new HttpResponse({
                        body: [new RegistrationCategory(123)],
                        headers
                    })
                )
            );

            // WHEN
            comp.ngOnInit();

            // THEN
            expect(service.query).toHaveBeenCalled();
            expect(comp.registrationCategories[0]).toEqual(jasmine.objectContaining({ id: 123 }));
        });
    });
});
