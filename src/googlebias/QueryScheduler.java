/* 
 * All content copyright Terracotta, Inc., unless otherwise indicated. All rights reserved. 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 * 
 */
package googlebias;

import static org.quartz.TriggerBuilder.newTrigger;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.apache.log4j.BasicConfigurator;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.JobBuilder.newJob;


/**
 * This Example will demonstrate how to start and shutdown the Quartz scheduler
 * and how to schedule a job to run in Quartz.
 *
 * @author Bill Kratzer
 */
public class QueryScheduler {

    public void run() throws Exception {
        // First we must get a reference to a scheduler
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();

     
        // define the job and tie it to our HelloJob class
        JobDetail job = newJob(GoogleBias.class).withIdentity("job1", "group1").build();

        // Trigger the job to run on the next round minute
        //Trigger trigger = newTrigger().withIdentity("trigger1", "group1").startAt(runTime).build();
        Trigger trigger = newTrigger().withIdentity("trigger1", "group1").
                startNow().
                withSchedule(
                        simpleSchedule().withIntervalInSeconds(1200)
                        .repeatForever()).build();

        // Tell quartz to schedule the job using our trigger
        sched.scheduleJob(job, trigger);

        // Start up the scheduler (nothing can actually run until the
        // scheduler has been started)
        sched.start();      
    }

    public static void main(String[] args) throws Exception {

        QueryScheduler querySched = new QueryScheduler();
        BasicConfigurator.configure();
        querySched.run();
        
         /*Calendar cal = Calendar.getInstance();
       // cal.setTime(date);
       // cal.add(Calendar.MINUTE, 5);
        Date newDate = cal.getTime();
        System.out.println(newDate.toString());*/

    }

}
