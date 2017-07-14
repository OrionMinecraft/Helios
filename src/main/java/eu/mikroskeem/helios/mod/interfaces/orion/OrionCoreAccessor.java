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

package eu.mikroskeem.helios.mod.interfaces.orion;

import eu.mikroskeem.shuriken.instrumentation.methodreflector.MethodReflector;
import eu.mikroskeem.shuriken.instrumentation.methodreflector.TargetFieldGetter;
import eu.mikroskeem.shuriken.instrumentation.methodreflector.TargetMethod;
import eu.mikroskeem.shuriken.reflect.Reflect;

import java.util.List;
import java.util.stream.Collectors;


/**
 * Accesses Orion internals via reflection
 *
 * TODO: expose mod/mixin configurations
 *
 * @author Mark Vainomaa
 */
public interface OrionCoreAccessor {
    @TargetFieldGetter("mixinConfigurations") List<String> getMixinConfigurations();
    @TargetFieldGetter("mods") List<Object> gm();

    @TargetMethod("_INVALID_getMods")
    default List<OrionModContainer> getMods() {
        return gm().stream()
                .map(Reflect::wrapInstance)
                .map(wi -> MethodReflector.newInstance(wi, OrionModContainer.class).getReflector())
                .collect(Collectors.toList());
    }
}
