import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IRegistrationCategory } from 'app/shared/model/registration-category.model';
import { RegistrationCategoryService } from './registration-category.service';

@Component({
    selector: 'jhi-registration-category-delete-dialog',
    templateUrl: './registration-category-delete-dialog.component.html'
})
export class RegistrationCategoryDeleteDialogComponent {
    registrationCategory: IRegistrationCategory;

    constructor(
        private registrationCategoryService: RegistrationCategoryService,
        public activeModal: NgbActiveModal,
        private eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.registrationCategoryService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'registrationCategoryListModification',
                content: 'Deleted an registrationCategory'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-registration-category-delete-popup',
    template: ''
})
export class RegistrationCategoryDeletePopupComponent implements OnInit, OnDestroy {
    private ngbModalRef: NgbModalRef;

    constructor(private activatedRoute: ActivatedRoute, private router: Router, private modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ registrationCategory }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(RegistrationCategoryDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.registrationCategory = registrationCategory;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
