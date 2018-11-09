/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { EventAppTestModule } from '../../../test.module';
import { ImpressionUpdateComponent } from 'app/entities/impression/impression-update.component';
import { ImpressionService } from 'app/entities/impression/impression.service';
import { Impression } from 'app/shared/model/impression.model';

describe('Component Tests', () => {
    describe('Impression Management Update Component', () => {
        let comp: ImpressionUpdateComponent;
        let fixture: ComponentFixture<ImpressionUpdateComponent>;
        let service: ImpressionService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [EventAppTestModule],
                declarations: [ImpressionUpdateComponent]
            })
                .overrideTemplate(ImpressionUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(ImpressionUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(ImpressionService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Impression(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.impression = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Impression();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.impression = entity;
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
