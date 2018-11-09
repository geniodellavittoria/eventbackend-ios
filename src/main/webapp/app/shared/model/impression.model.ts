import { IEvent } from 'app/shared/model//event.model';

export interface IImpression {
    id?: number;
    imageContentType?: string;
    image?: any;
    eventIds?: IEvent[];
}

export class Impression implements IImpression {
    constructor(public id?: number, public imageContentType?: string, public image?: any, public eventIds?: IEvent[]) {}
}
