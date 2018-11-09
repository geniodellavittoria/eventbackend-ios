/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { EventAppTestModule } from '../../../test.module';
import { RegistrationCategoryUpdateComponent } from 'app/entities/registration-category/registration-category-update.component';
import { RegistrationCategoryService } from 'app/entities/registration-category/registration-category.service';
import { RegistrationCategory } from 'app/shared/model/registration-category.model';

describe('Component Tests', () => {
    describe('RegistrationCategory Management Update Component', () => {
        let comp: RegistrationCategoryUpdateComponent;
        let fixture: ComponentFixture<RegistrationCategoryUpdateComponent>;
        let service: RegistrationCategoryService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EventAppTestModule],
                declarations: [RegistrationCategoryUpdateComponent]
            })
                .overrideTemplate(RegistrationCategoryUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(RegistrationCategoryUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(RegistrationCategoryService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new RegistrationCategory(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.registrationCategory = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new RegistrationCategory();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.registrationCategory = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
