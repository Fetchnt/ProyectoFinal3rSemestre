import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Traductor } from './traductor';

describe('Traductor', () => {
  let component: Traductor;
  let fixture: ComponentFixture<Traductor>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Traductor],
    }).compileComponents();

    fixture = TestBed.createComponent(Traductor);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
