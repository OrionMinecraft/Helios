/*
 * This file is part of project Helios, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2017 Mark Vainomaa <mikroskeem@mikroskeem.eu>
 * Copyright (c) Contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package eu.mikroskeem.helios.mod.mixins.core;

import eu.mikroskeem.orion.api.OrionAPI;
import eu.mikroskeem.orion.api.mod.ModInfo;
import net.minecraft.server.v1_12_R1.CrashReport;
import net.minecraft.server.v1_12_R1.CrashReportSystemDetails;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


/**
 * Mixin to inject Orion/Helios info into crash report
 *
 * @author Mark Vainomaa
 */
@Mixin(value = CrashReport.class, remap = false)
public abstract class MixinCrashReport {
    @Shadow @Final private CrashReportSystemDetails d;

    @Inject(method = "h", at = @At("RETURN"))
    private void onPopulateEnvironment(CallbackInfo cb) {
        this.d.a("Orion & Helios - What are those?", () -> new StringBuilder(64)
            .append('\n')
            .append("\t\tOrion is a Paper server coremod framework, written using SpongeMixin library\n")
            .append("\t\tHelios is modification collection for Paper\n\n")
            .append("\t\tSteps to do before reporting an issue:\n")
            .append("\t\t- First try to figure out whether issue is caused by a coremod or not,\n")
            .append("\t\t\tand then report issue to coremod developer\n")
            .append("\t\t- Do old school method - disable coremods one-by-one, and find the bastard who caused this crash\n")
            .append("\t\t- Disable Helios, and for last Orion, and see if server keeps crashing\n")
            .append("\t\t- If issue is caused by Helios, report issue to https://github.com/OrionMinecraft/Helios\n")
            .append("\t\t- If issue is caused by Orion, report issue to https://github.com/OrionMinecraft/Orion\n")
            .append("\t\t- If issue is caused by Paper, report issue to https://github.com/PaperMC/Paper\n")
            .append("\t\tThank you for your cooperation and reading :-)")
            .toString()
        );

        this.d.a("Orion coremods", () -> {
            StringBuilder result = new StringBuilder(64);
            for (ModInfo modInfo : OrionAPI.getInstance().getMods())
                result.append("\n\t\t").append(modInfo.getId());
            return result.toString();
        });

        /* TODO: Look into Mixin files and point out potential problem cause */
        this.d.a("Orion registered mixins", () -> {
            StringBuilder result = new StringBuilder(64);
            for(String mixinConfig: OrionAPI.getInstance().getMixinConfigurations()) {
                result.append("\n\t\t").append(mixinConfig);
            }
            return result.toString();
        });

        this.d.a("Orion downloaded libraries", () -> {
            StringBuilder result = new StringBuilder(64);
            for(String library : OrionAPI.getInstance().getRegisteredLibraries())
                result.append("\n\t\t").append(library);
            return result.toString();
        });
    }
}
