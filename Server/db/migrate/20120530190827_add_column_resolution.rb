class AddColumnResolution < ActiveRecord::Migration
  def up
  	add_column :draws, :resolution, :integer, :default => 0
  end

  def down
  end
end
