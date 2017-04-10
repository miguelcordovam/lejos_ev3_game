package com.miguel.game;

import java.util.Random;

import lejos.hardware.BrickFinder;
import lejos.hardware.Keys;
import lejos.hardware.ev3.EV3;
import lejos.hardware.motor.EV3LargeRegulatedMotor;
import lejos.hardware.motor.EV3MediumRegulatedMotor;
import lejos.hardware.port.MotorPort;
import lejos.hardware.port.SensorPort;
import lejos.hardware.sensor.EV3TouchSensor;
import lejos.hardware.sensor.SensorMode;

public class Game {

	private static int OFFSET = 60;

	public static void main(String[] args) throws Exception {
		EV3LargeRegulatedMotor LEFT_MOTOR = new EV3LargeRegulatedMotor(
				MotorPort.B);
		EV3LargeRegulatedMotor RIGHT_MOTOR = new EV3LargeRegulatedMotor(
				MotorPort.C);

		EV3MediumRegulatedMotor GRABBER_MOTOR = new EV3MediumRegulatedMotor(
				MotorPort.A);

		EV3TouchSensor TOUCH_SENSOR = new EV3TouchSensor(SensorPort.S1);

		EV3 ev3Brick = (EV3) BrickFinder.getLocal();
		Keys buttons = ev3Brick.getKeys();

		EV3LargeRegulatedMotor[] motors = new EV3LargeRegulatedMotor[2];
		motors[0] = LEFT_MOTOR;
		motors[1] = RIGHT_MOTOR;
		
		SensorMode touch = TOUCH_SENSOR.getTouchMode();
		float[] sample = new float[touch.sampleSize()];

		while (buttons.getButtons() != Keys.ID_ESCAPE) {

			touch.fetchSample(sample, 0);

			if (sample[0] == 1) {
				rotateRandomly(motors);
				grabAndSee(GRABBER_MOTOR);
			}
		}

		LEFT_MOTOR.close();
		RIGHT_MOTOR.close();
		GRABBER_MOTOR.close();
		TOUCH_SENSOR.close();
	}

	private static void rotateRandomly(EV3LargeRegulatedMotor[] motors) {
		motors[0].setSpeed(300);
		motors[1].setSpeed(300);

		Random random = new Random();

		for (int i = 0; i < random.nextInt(10) + 1; i++) {
			int motorToRotate = random.nextInt(2);
			int numberOfRotations = random.nextInt(5) + 1;
			boolean clockwise = random.nextBoolean();

			rotate(numberOfRotations, motors[motorToRotate], clockwise);
		}
	}

	private static void rotate(int numberOfRotations,
			EV3LargeRegulatedMotor motor, boolean clockwise) {
		int direction = clockwise ? 1 : -1;
		motor.rotate(direction * (180 * numberOfRotations + OFFSET));
		motor.rotate(-1 * direction * OFFSET);
	}

	private static void grabAndSee(EV3MediumRegulatedMotor grabber) {
		grabber.setSpeed(200);
		grabber.rotateTo(220);
		grabber.rotateTo(-10);
	}
}