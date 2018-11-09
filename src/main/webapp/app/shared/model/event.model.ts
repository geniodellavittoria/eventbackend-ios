import { Moment } from 'moment';
import { ICategory } from 'app/shared/model//category.model';
import { IUserEventRegistration } from 'app/shared/model//user-event-registration.model';
import { IImpression } from 'app/shared/model//impression.model';

export interface IEvent {
    id?: number;
    name?: string;
    description?: any;
    locationLongitude?: number;
    locationLatitude?: number;
    price?: number;
    timestamp?: Moment;
    eventStart?: Moment;
    eventEnd?: Moment;
    eventImageContentType?: string;
    eventImage?: any;
    category?: ICategory;
    userEventRegistrationIds?: IUserEventRegistration[];
    impression?: IImpression;
}

export class Event implements IEvent {
    constructor(
        public id?: number,
        public name?: string,
        public description?: any,
        public locationLongitude?: number,
        public locationLatitude?: number,
        public price?: number,
        public timestamp?: Moment,
        public eventStart?: Moment,
        public eventEnd?: Moment,
        public eventImageContentType?: string,
        public eventImage?: any,
        public category?: ICategory,
        public userEventRegistrationIds?: IUserEventRegistration[],
        public impression?: IImpression
    ) {}
}
