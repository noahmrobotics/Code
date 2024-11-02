/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

import java.util.List;

@Autonomous(name="AutonTest", group="Iterative Opmode")

public class AutonTesting extends OpMode {
    // Declare OpMode members.
    //private ElapsedTime runtime = new ElapsedTime();
    private Drive drive;
    private Sensors sensors;
    private Intake intake;
    private Vision vision;
    private Orientation angles = null;
    private Limelight3A limelight;

    private String openCVPosition;
    private int autoCase = 1;
    private int encoderTarget = 0;
    private int rightEncoderTarget = 0;
    private int leftEncoderTarget = 0;

    /*
     * Code to run ONCE when the driver hits INIT
     */
    @Override
    public void init() {
        drive = new Drive(hardwareMap);
        sensors = new Sensors(hardwareMap);
        intake = new Intake(hardwareMap);
        vision = new Vision();
        limelight = hardwareMap.get(Limelight3A.class, "Limelight");
        telemetry.setMsTransmissionInterval(10);
        limelight.pipelineSwitch(4);
        limelight.start();

        telemetry.addData("Status", "Initialized");
    }

    /*
     * Code to run REPEATEDLY after the driver hits INIT, but before they hit PLAY
     */
    @Override
    public void init_loop() {
    }

    /*
     * Code to run ONCE when the driver hits PLAY
     */
    @Override
    public void start() {
        //runtime.reset();
        intake.closeFrontGripper();

        angles = sensors.readGyroAngle();

        openCVPosition = vision.getCvMarkerLocation().toString();

    }

    /*
     * Code to run REPEATEDLY after the driver hits PLAY but before they hit STOP
     */
    @Override
    public void loop() {
        //Get gyro reading
        angles = sensors.readGyroAngle();

        // ---Limelight Readings---\\
    // ---AprilTag Detection---\\
        LLResult result = limelight.getLatestResult();
        if (result != null && result.isValid()) {
            double tx = result.getTx(); // How far left or right the target is (degrees)
            double ty = result.getTy(); // How far up or down the target is (degrees)
            double ta = result.getTa(); // How big the target looks (0%-100% of the image)

            telemetry.addData("Target X", tx);
            telemetry.addData("Target Y", ty);
            telemetry.addData("Target Area", ta);
        } else {
            telemetry.addData("Limelight", "No Targets");
        }



        switch (autoCase) {
            case 1:
                // Set Encoder Targets
                leftEncoderTarget = 400;
                rightEncoderTarget = 400;
                drive.leftEncoderToPosition(leftEncoderTarget);
                drive.rightEncoderToPosition(rightEncoderTarget);
                // Next Case
                autoCase = 2;
                break;
            case 2:
                // Drive To Encoder Target
                drive.straightDrive(.4);
                // Stop
                if(drive.getRightEncoderValue() >= rightEncoderTarget && drive.getLeftEncoderValue() >= leftEncoderTarget) {
                    drive.straightDrive(0);
                    // Next Case
                    autoCase = 3;
                }
                break;

            case 3:
                // Drive To Encoder Target
                drive.straightDrive(.4);
                // Stop
                if(drive.getRightEncoderValue() >= rightEncoderTarget && drive.getLeftEncoderValue() >= leftEncoderTarget) {
                    drive.straightDrive(0);
                    // Next Case
                    autoCase = 3;
                }
                break;

            default:
                drive.teleopDrive(0, 0);
                break;

            }

       telemetry.addData("Gyro Z", sensors.getGyroZ(angles));

      //  telemetry.addData("Lift Pos", intake.getEncodedLift());
     //  telemetry.addData("Lift Busy", intake.liftBusy());
     //   telemetry.addData("Lift Level", intake.getLiftLevel());
        telemetry.addData("LR Position", drive.getLeftEncoderValue());
        telemetry.addData("LR Drive Busy", drive.leftMotorBusy());
        telemetry.addData("LR Position", drive.getRightEncoderValue());
        telemetry.addData("RR Drive Busy", drive.rightMotorBusy());


    }

    /*
     * Code to run ONCE after the driver hits STOP
     */
    @Override
    public void stop() {
    }


}


