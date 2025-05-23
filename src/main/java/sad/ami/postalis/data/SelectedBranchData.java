package sad.ami.postalis.data;

import com.mojang.serialization.Codec;
import lombok.Builder;

@Builder(toBuilder = true)
public record SelectedBranchData() {
    public static final Codec<SelectedBranchData> CODEC = Codec.unit(SelectedBranchData::new);

}
