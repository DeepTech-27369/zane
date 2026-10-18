package org.firstinspires.ftc.teamcode.opmode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

/*
 * Tank Drive TeleOp
 *
 * Hardware:
 *   Control Hub motor port 0: "backRightMotor"
 *   Control Hub motor port 1: "backLeftMotor"
 *   Control Hub motor port 2: "intakeMotor"
 *
 * Controls:
 *   Gamepad 1 (driver)
 *     Left stick Y   = drive forward / backward
 *     Left stick X   = turn left / right while driving (curves)
 *     Right stick X  = spin in place (overrides the left stick while held)
 *     Hold left bumper  = slow mode (precise driving)
 *   Gamepad 1 or Gamepad 2 (operator)
 *     Right trigger  = intake in
 *     Left trigger   = intake out
 *
 * If the robot drives backwards or spins when the left stick is pushed forward,
 * flip the directions in the "Motor directions" section below.
 */
@TeleOp(name = "Tank Drive", group = "TeleOp")
public class TankDrive extends LinearOpMode {

    // Max drive speed in slow mode (0.0 - 1.0)
    static final double SLOW_MODE_SPEED = 0.4;
    // Max intake speed (0.0 - 1.0)
    static final double INTAKE_SPEED = 1.0;
    // Stick values smaller than this are ignored so the robot doesn't creep
    static final double DEADZONE = 0.05;

    @Override
    public void runOpMode() {
        DcMotor backLeftMotor = hardwareMap.get(DcMotor.class, "left");
        DcMotor backRightMotor = hardwareMap.get(DcMotor.class, "right");
        DcMotor intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");

        // Motor directions: the two motors face opposite ways, so reverse the left one.
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        intakeMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        DcMotor[] driveMotors = {backLeftMotor, backRightMotor};
        for (DcMotor motor : driveMotors) {
            motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
            // BRAKE makes the robot stop quickly when you let go of the sticks
            motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
        intakeMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        telemetry.addLine("Ready. Press START.");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {
            // ----- Drive -----
            // Pushing a stick forward gives a negative Y value, so negate it.
            double drive = applyDeadzone(-gamepad1.left_stick_y);
            double turn = applyDeadzone(gamepad1.left_stick_x);
            double spin = applyDeadzone(gamepad1.right_stick_x);

            double leftPower;
            double rightPower;
            if (spin != 0) {
                // Right stick: wheels go opposite ways so the robot turns in place
                leftPower = spin;
                rightPower = -spin;
            } else {
                // Left stick: forward/back plus turning
                leftPower = drive + turn;
                rightPower = drive - turn;
                // Scale down so neither side goes past full power
                double max = Math.max(1.0, Math.max(Math.abs(leftPower), Math.abs(rightPower)));
                leftPower /= max;
                rightPower /= max;
            }

            if (gamepad1.left_bumper) {
                leftPower *= SLOW_MODE_SPEED;
                rightPower *= SLOW_MODE_SPEED;
            }

            backLeftMotor.setPower(leftPower);
            backRightMotor.setPower(rightPower);

            // ----- Intake -----
            double intakeIn = Math.max(gamepad1.right_trigger, gamepad2.right_trigger);
            double intakeOut = Math.max(gamepad1.left_trigger, gamepad2.left_trigger);
            double intakePower = (intakeIn - intakeOut) * INTAKE_SPEED;
            intakeMotor.setPower(intakePower);

            // ----- Telemetry -----
            telemetry.addData("Mode", gamepad1.left_bumper ? "SLOW" : "NORMAL");
            telemetry.addData("Left power", "%.2f", leftPower);
            telemetry.addData("Right power", "%.2f", rightPower);
            telemetry.addData("Intake power", "%.2f", intakePower);
            telemetry.update();
        }

        for (DcMotor motor : driveMotors) {
            motor.setPower(0);
        }
        intakeMotor.setPower(0);
    }

    private double applyDeadzone(double value) {
        return Math.abs(value) < DEADZONE ? 0 : value;
    }
}
