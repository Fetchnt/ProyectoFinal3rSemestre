import { ComponentFixture, TestBed } from '@angular/core/testing';

import { Runcode } from './runcode';

describe('Runcode', () => {
  let component: Runcode;
  let fixture: ComponentFixture<Runcode>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [Runcode],
    }).compileComponents();

    fixture = TestBed.createComponent(Runcode);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
